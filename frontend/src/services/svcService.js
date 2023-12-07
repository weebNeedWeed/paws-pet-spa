import BaseService from "./BaseService";

class SvcService extends BaseService {
  getAll() {
    return this.axiosInstance.get("/services");
  }
}

const svcService = new SvcService();
export default svcService;
