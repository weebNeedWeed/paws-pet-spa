import { useMutation } from "react-query";
import customerService from "../services/customerService";
import useAccessToken from "./useAccessToken";

function useMakeAppointment() {
  const [accessToken] = useAccessToken();

  const result = useMutation((params) =>
    customerService.makeAppointment(accessToken, params)
  );

  return result;
}

export default useMakeAppointment;
