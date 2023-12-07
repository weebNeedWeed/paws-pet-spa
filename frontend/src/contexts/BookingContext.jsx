import { createContext, useReducer } from "react";
import PropTypes from "prop-types";
import { useContext } from "react";

// The booking process includes 3 steps:
// Providing default infos, booking services for each pet and selecting booking time

const initialStates = {
  location: "",
  petCount: 0,
  note: "",
  registeredPets: [],
  step: 1,
  handlingPetNum: 0,
};
const reducer = (state, action) => {
  switch (action.type) {
    case "UPDATE_BASIC_INFORMATION": {
      const { location, petCount, note } = action.payload;
      return {
        ...state,
        step: 2,
        handlingPetNum: 1,
        location,
        petCount,
        note,
      };
    }

    case "UPDATE_PET_INFORMATION": {
      const { name, service, petType } = action.payload;
      const registeredPetsClone = [...state.registeredPets];
      registeredPetsClone.push({
        petName: name,
        petTypeId: petType,
        serviceIds: service,
      });

      const nextHandlingPetNum = state.handlingPetNum + 1;

      return {
        ...state,
        handlingPetNum: nextHandlingPetNum,
        registeredPets: registeredPetsClone,
        step: nextHandlingPetNum > state.petCount ? 3 : state.step,
      };
    }

    default:
      return { ...state };
  }
};

const BookingContext = createContext();

export const BookingContextProvider = ({ children }) => {
  const [state, dispatch] = useReducer(reducer, initialStates);

  return (
    <BookingContext.Provider value={[state, dispatch]}>
      {children}
    </BookingContext.Provider>
  );
};

BookingContextProvider.propTypes = {
  children: PropTypes.node,
};

export const useBookingContext = () => {
  const result = useContext(BookingContext);
  if (!result) {
    throw new Error("useBookingContext must be used within BookingContext");
  }

  return result;
};
