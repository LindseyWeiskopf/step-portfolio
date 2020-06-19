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
  private List<TimeRange> busyTimes = new ArrayList<>();
  private List<TimeRange> freeTimes = new ArrayList<>();

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    busyTimes = getBusyTimes(events, request);
    return (getFreeTimes(findOverlappingTimes(), request));
  }

  // Compare attendees of given events with attendees for requested meeting to determine conflicts
  private List<TimeRange> getBusyTimes(Collection<Event> events, MeetingRequest request) {

    Collection<String> attendees = request.getAttendees();
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
    List<TimeRange> overlappingTimes = new ArrayList<>();
    // variables used to keep track of start and end times of previous relevant busy time blocks
    int prevStartTime = -1;
    int prevEndTime = 0;
    for (TimeRange timeRange : busyTimes) {
      if (prevStartTime == -1) {
        prevStartTime = timeRange.start();
        prevEndTime = timeRange.end();
      }
      // events overlap
      else if ((timeRange.start() <= prevEndTime) && (timeRange.end() >= prevEndTime)){
        prevEndTime = timeRange.end();
      }
      // events do not overlap
      else if (timeRange.start() > prevEndTime) {
        overlappingTimes.add(TimeRange.fromStartEnd(prevStartTime, prevEndTime, false));
        prevStartTime = timeRange.start();
        prevEndTime = timeRange.end();
      } 
    } 
    overlappingTimes.add(TimeRange.fromStartEnd(prevStartTime, prevEndTime, false));
    return overlappingTimes;
  }
  
  // Gets list of time slots where no attendess have any meetings >= duration time
  private Collection<TimeRange> getFreeTimes(List<TimeRange> overlappingTimes, MeetingRequest request) {
    // variables used to keep track of start and end times of previous relevant busy time blocks
    int prevStartTime = 0;
    int prevEndTime = 0;
    for (TimeRange timeRange : overlappingTimes) {
      if (timeRange.start() == 0) {
        prevStartTime = timeRange.end();
      }
      else {
        prevEndTime = timeRange.start();
        if ((prevEndTime - prevStartTime) >= request.getDuration()) { 
          freeTimes.add(TimeRange.fromStartEnd(prevStartTime, prevEndTime, false));
        }
        prevStartTime = timeRange.end();
      }
    }
    if ((TimeRange.END_OF_DAY - prevStartTime) >= request.getDuration()) { 
      freeTimes.add(TimeRange.fromStartEnd(prevStartTime, TimeRange.END_OF_DAY, true));
    }
    return freeTimes;
  }
}
