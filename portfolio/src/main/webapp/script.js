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





// Get Random greeting using arrow function -- fetches greeting from server and displays on the page
function getRandomGreeting() {
  fetch('/data').then(response => response.json()).then((greeting) => {
    const listElement = document.getElementById('greeting-container');
    listElement.innerHTML = '';

    var i;
    for(i=0; i<greeting.length; i++) {
      listElement.appendChild(
          createListElement(greeting[i]));
    }
  });
}

// Creates an <li> element containing text.
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

// Creates a map and adds it to the page.
function initMap() {
  // Coordinates center map  around Baltimore, MD
  var myLatlng = new google.maps.LatLng(39.412, -76,775);
  var mapOptions = {
    center: myLatlng, 
    zoom: 12, 
    mapTypeId: 'roadmap'
  };
  const map = new google.maps.Map(document.getElementById('map'), mapOptions);
}

