import BaseService from "./BaseService";

class PetTypeService extends BaseService {
  getAll() {
    return this.axiosInstance.get("/pet-types");
  }
}

const petTypeService = new PetTypeService();
export default petTypeService;
