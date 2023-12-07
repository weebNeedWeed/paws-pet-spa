import BaseService from "./BaseService";

class CustomerService extends BaseService {
  getProfile(accessToken) {
    this.axiosInstance.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${accessToken}`;
    return this.axiosInstance.get("/customer");
  }

  makeAppointment(accessToken, makeAppointmentRequest) {
    const { location, time, note, appointmentItems } = makeAppointmentRequest;

    this.axiosInstance.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${accessToken}`;

    return this.axiosInstance.post("/customer/appointments", {
      location,
      time,
      note,
      appointmentItems,
    });
  }
}

const customerService = new CustomerService();

export default customerService;
