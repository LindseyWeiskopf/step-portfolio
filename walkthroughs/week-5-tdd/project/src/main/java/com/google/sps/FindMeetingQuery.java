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

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    busyTimes = getBusyTimes();
    findOverlappingTimes();
    getFreeTimes();
    //throw new UnsupportedOperationException("TODO: Implement this method.");
    /*Collection<String> attendees = request.getAttendees();
    long duration = request.getDuration();
    for (Event event : events) {
      if(Collections.disjoint(event.getAttendees(), attendees)) {
        busyTimes.add(event.getWhen());
      }
    }

    Collections.sort(busyTimes, TimeRange.ORDER_BY_START);
    int earliest = TimeRange.START_OF_DAY;
    int index = 0;
    Collection<TimeRange> freeTimes = new ArrayList<TimeRange>();
    TimeRange firstStart = TimeRange.WHOLE_DAY;
    while(index < busyTimes.size()) {
    firstStart = busyTimes.get(index);
    TimeRange freeTimeRange = TimeRange.fromStartEnd(earliest, firstStart.start(), false);
  
    
    freeTimes.add(freeTimeRange);
    int endTime = firstStart.end(); 
    index++;
    earliest = endTime;
    firstStart = busyTimes.get(index);
    }
    freeTimes.add(TimeRange.fromStartEnd(firstStart.start(), TimeRange.END_OF_DAY, true));
    return freeTimes;*/
  }

  private List<TimeRange> getBusyTimes(Collection<Event> events, MeetingRequest request) {
    Collection<String> attendees = getAttendees;
    for (Event event : events) {
      if(Collections.disjoint(event.getAttendees(), attendees)) {
        busyTimes.add(event.getWhen());
      }
    }
    Collections.sort(busyTimes, TimeRange.ORDER_BY_START);
    return busyTimes;
  }

  private List<TimeRange> findOverlappingTimes() {
    List<TimeRange> overlappingTimes = new ArrayList<>();
    int tempStartTime = 0;
    int tempEndTime = 0;
    for (TimeRange timeRange : busyTimes) {
      if (tempStartTime == 0) {
        tempStartTime = timeRange.start();
        tempEndTime = timeRange.end();
      }
      else if ((timeRange.start() <= tempEndTime) && (timeRange.end() >= tempEndTime)){
        tempEndTime = timeRange.end();
      }
      else if (timeRange.start() > tempEndTime) {
        fromStartEnd(tempEndTime, tempEndTime, false);
        tempStartTime = timeRange.start();
        tempEndTime = timeRange.end();
      } 
      else {
        ;
      }
    } 

  }

  private Collection<TimeRange> getFreeTimes() {

  }
}
