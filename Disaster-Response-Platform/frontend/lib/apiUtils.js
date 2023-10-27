import axios from 'axios';

// Define a base URL for your API
const baseURL = 'https://localhost:8000';

export const api = axios.create({ baseURL });

