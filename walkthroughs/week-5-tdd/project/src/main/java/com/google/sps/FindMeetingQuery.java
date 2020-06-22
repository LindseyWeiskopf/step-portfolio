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
    if (busyTimes.size() > 0) {
      return (getFreeTimes(findOverlappingTimes(), request));
    }
    return (getFreeTimes(busyTimes, request));
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
    // Variables used to keep track of start and end times of previous relevant busy time blocks
    int prevStart = busyTimes.get(0).start();
    int prevEnd = busyTimes.get(0).end();
    for (int i = 1; i < busyTimes.size(); i++) {
      int currentStart = busyTimes.get(i).start();
      int currentEnd = busyTimes.get(i).end();
      if (currentStart > prevEnd) {
        overlappingTimes.add(TimeRange.fromStartEnd(prevStart, prevEnd, false));
        prevStart = currentStart;
        prevEnd = currentEnd;
      }
      else if (currentEnd > prevEnd) {
        prevEnd = currentEnd;
      }
    }
    overlappingTimes.add(TimeRange.fromStartEnd(prevStart, prevEnd, false));
    return overlappingTimes;
  }
  
  // Gets list of time slots where no attendess have any meetings >= duration time
  private Collection<TimeRange> getFreeTimes(List<TimeRange> overlappingTimes, MeetingRequest request) {
    // Variables used to keep track of start and end times of previous relevant busy time blocks
    int prevStart = 0;
    int prevEnd = 0;
    for (TimeRange timeRange : overlappingTimes) {
      int currentStart = timeRange.start();
      int currentEnd = timeRange.end();
      if (currentStart == 0) {
        prevStart = currentEnd;
      }
      else {
        prevEnd = currentStart;
        if ((prevEnd - prevStart) >= request.getDuration()) { 
          freeTimes.add(TimeRange.fromStartEnd(prevStart, prevEnd, false));
        }
        prevStart = currentEnd;
      }
    }
    if ((TimeRange.END_OF_DAY - prevStart) >= request.getDuration()) { 
      freeTimes.add(TimeRange.fromStartEnd(prevStart, TimeRange.END_OF_DAY, true));
    }
    return freeTimes;
  }
}
