import { useMutation } from "react-query";
import authenticationService from "./../services/authenticationService";

function useRegister() {
  const result = useMutation((data) => authenticationService.register(data));
  return result;
}

export default useRegister;
