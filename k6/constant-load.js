import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
    vus: 200, // number of virtual users
    duration: '10m', // duration of the test
};

export default function () {
    http.get('https://demo-petstorepetservice-eastus-fazdate.azurewebsites.net/petstorepetservice/v2/pet/info');
    sleep(0); // Adjust the sleep time as needed
}
