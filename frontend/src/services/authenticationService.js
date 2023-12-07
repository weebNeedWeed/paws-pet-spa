import BaseService from "./BaseService";

class AuthenticationService extends BaseService {
  login(params) {
    return this.axiosInstance.post("/customer/login", params);
  }

  register(params) {
    return this.axiosInstance.post("/customer/register", params);
  }
}

const authenticationService = new AuthenticationService();

export default authenticationService;
