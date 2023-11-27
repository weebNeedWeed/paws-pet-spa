import { useMutation } from "react-query";
import authenticationService from "../services/authenticationService";

// the query is disabled by default
export default function useLogin() {
  const result = useMutation((params) => authenticationService.login(params));

  return result;
}
