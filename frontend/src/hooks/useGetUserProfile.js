import { useQuery } from "react-query";
import customerService from "../services/customerService";
import useAccessToken from "./useAccessToken";

function useGetUserProfile() {
  const [accessToken] = useAccessToken();

  const result = useQuery(
    "profile",
    () => customerService.getProfile(accessToken),
    {
      retry: false,
      retryOnMount: false,
      enabled: false,
    }
  );

  return result;
}

export default useGetUserProfile;
