import axios from "axios";

class AuthenticationService {
  axiosInstance;

  constructor() {
    this.axiosInstance = axios.create({
      baseURL: import.meta.env.VITE_BASE_URL,
      timeout: 1000,
    });
  }

  login(params) {
    return this.axiosInstance.post("/customers/login", params);
  }

  register(params) {
    return this.axiosInstance.post("/customers/register", params);
  }
}

const authenticationService = new AuthenticationService();

export default authenticationService;
