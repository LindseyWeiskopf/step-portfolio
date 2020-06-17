// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Comparator;

public final class FindMeetingQuery {
  List<TimeRange> busyTimes = new ArrayList<>();
  List<TimeRange> overlappingTimes = new ArrayList<>();
  List<TimeRange> freeTimes = new ArrayList<>();

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    busyTimes = getBusyTimes(events, request, true);
    overlappingTimes = findOverlappingTimes();
    getFreeTimes(request);
    if (freeTimes.isEmpty() && !(request.getAttendees().isEmpty())) {
      getBusyTimes(events, request, false);
      overlappingTimes = findOverlappingTimes();
      getFreeTimes(request);
    }
    return freeTimes;
  }

  // Compare attendees of given events with attendees for requested meeting to determine conflicts
  private List<TimeRange> getBusyTimes(Collection<Event> events, MeetingRequest request, boolean withOptionalAttendees) {
    busyTimes.clear();
    Collection<String> attendees = new ArrayList<>();
    attendees.clear();
    attendees.addAll(request.getAttendees());
    if (withOptionalAttendees) {
      attendees.addAll(request.getOptionalAttendees());
    }
    for (Event event : events) {
      if(!(Collections.disjoint(event.getAttendees(), attendees))) {
        busyTimes.add(event.getWhen());
      }
    }
    Collections.sort(busyTimes, TimeRange.ORDER_BY_START);
    return busyTimes;
  }

  // Returns list of consolidated busy time ranges accounting for overlapping events
  private List<TimeRange> findOverlappingTimes() {
    overlappingTimes.clear();
    int tempStartTime = -1;
    int tempEndTime = 0;
    for (TimeRange timeRange : busyTimes) {
      if (tempStartTime == -1) {
        tempStartTime = timeRange.start();
        tempEndTime = timeRange.end();
      }
      else if ((timeRange.start() <= tempEndTime) && (timeRange.end() >= tempEndTime)){
        tempEndTime = timeRange.end();
      }
      else if (timeRange.start() > tempEndTime) {
        overlappingTimes.add(TimeRange.fromStartEnd(tempStartTime, tempEndTime, false));
        tempStartTime = timeRange.start();
        tempEndTime = timeRange.end();
      } 
      else {
        ;
      }
    } 
    overlappingTimes.add(TimeRange.fromStartEnd(tempStartTime, tempEndTime, false));
    return overlappingTimes;
  }
  
  // Gets list of time slots where no attendess have any meetings >= duration time
  private void getFreeTimes(MeetingRequest request) {
    freeTimes.clear();
    int tempStartTime = 0;
    int tempEndTime = 0;
    for (TimeRange timeRange : overlappingTimes) {
      if (timeRange.start() == 0) {
        tempStartTime = timeRange.end();
      }
      else {
        tempEndTime = timeRange.start();
        if ((tempEndTime - tempStartTime) >= request.getDuration()) { 
          freeTimes.add(TimeRange.fromStartEnd(tempStartTime, tempEndTime, false));
        }
        tempStartTime = timeRange.end();
      }
    }
    if ((TimeRange.END_OF_DAY - tempStartTime) >= request.getDuration()) { 
      freeTimes.add(TimeRange.fromStartEnd(tempStartTime, TimeRange.END_OF_DAY, true));
    }
  }
}
