import { useQuery } from "react-query";
import petTypeService from "../services/petTypeService";

function useGetPetTypes() {
  const result = useQuery("pet-types", () => petTypeService.getAll(), {
    staleTime: Infinity,
  });
  return result;
}

export default useGetPetTypes;
