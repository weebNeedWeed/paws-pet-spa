import axios from "axios";

class BaseService {
  axiosInstance;

  constructor() {
    this.axiosInstance = axios.create({
      timeout: 1000,
      baseURL: import.meta.env.VITE_BASE_URL,
    });
  }
}

export default BaseService;
