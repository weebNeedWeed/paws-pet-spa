import { useQuery } from "react-query";
import svcService from "../services/svcService";

export default function useGetServices() {
  const result = useQuery("services", () => svcService.getAll(), {
    staleTime: Infinity,
  });
  return result;
}
