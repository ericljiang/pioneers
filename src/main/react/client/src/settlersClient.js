export default function getGreeting() {
  return fetch('/api/hello')
    .then(response => response.json());
}
