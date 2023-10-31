import axios from 'axios';

// Define a base URL for your API
const baseURL = 'http://3.218.226.215:8000';

export const api = axios.create({ baseURL });

