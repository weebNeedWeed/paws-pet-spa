import { createContext, useContext, useReducer } from "react";
import PropTypes from "prop-types";

const initialStates = {
  username: "",
  isAuthenticated: false,
};

const reducer = (state, action) => {
  switch (action.type) {
    case "AUTH_USER": {
      const { username } = action.payload;
      return { ...state, username, isAuthenticated: true };
    }

    case "LOGOUT_USER": {
      return { ...state, username: "", isAuthenticated: false };
    }

    default:
      return { ...state };
  }
};

const UserContext = createContext(null);

export const UserContextProvider = ({ children }) => {
  const [state, dispatch] = useReducer(reducer, initialStates);

  return (
    <UserContext.Provider value={[state, dispatch]}>
      {children}
    </UserContext.Provider>
  );
};
UserContextProvider.propTypes = {
  children: PropTypes.element,
};

export const useUserContext = () => {
  const result = useContext(UserContext);
  if (!result) {
    throw new Error("useUserContext must be used within UserContext");
  }

  return result;
};
