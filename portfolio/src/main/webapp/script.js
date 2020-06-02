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



/*

//Get Randome greeting using arrow function -- fetches greeting from server and displays on the page
function getRandomGreeting() {
  fetch('/data').then(response => response.text()).then((greeting) => {
    document.getElementById('greeting-container').innerText = greeting;
  });
}*/
function getComments() {
  fetch('/comments').then(response => response.json()).then((posts) => {
      const liElement = document.getElementById('greeting-container');
      posts.forEach((post) => {
        liElement.appendChild(createPostElement(post));
    })
  }); 
}

function createPostElement(post) {
  const postElement = document.createElement('li');
  postElement.className = 'post';

  const commentElement = document.createElement('span');
  commentElement.innerText = post.comment;

  postElement.appendChild(commentElement);

  return postElement;
}

 
function addRandomGreeting() {
  const greetings = 
     ['Hey, How are you?', 'Good Morning, friend!', 'Howdy, yall', 'Today\'s going to be the best day ever'];
  // Pick a random fact
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];
  // Add it to the page
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}



