import axios from 'axios';

// Define a base URL for your API
const baseURL = 'http://localhost:8000';

export const api = axios.create({ baseURL });

