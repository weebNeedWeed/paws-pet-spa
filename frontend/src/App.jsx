import { RouterProvider } from "react-router-dom";
import { createBrowserRouter } from "react-router-dom";
import DefaultLayout from "./components/ui/DefaultLayout";
import AuthLayout from "./components/ui/AuthLayout";
import HomePage from "./pages/home/HomePage";
import LoginPage from "./pages/authentication/LoginPage";
import RegisterPage from "./pages/authentication/RegisterPage";
import { QueryClient, QueryClientProvider } from "react-query";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { UserContextProvider } from "./contexts/UserContext";
import BookingPage from "./pages/booking/BookingPage";

const router = createBrowserRouter([
  {
    path: "/",
    element: <DefaultLayout />,
    children: [
      { index: true, element: <HomePage /> },
      { path: "booking", element: <BookingPage /> },
    ],
  },
  {
    element: <AuthLayout />,
    children: [
      {
        index: true,
        path: "/login",
        element: <LoginPage />,
      },
      {
        path: "/register",
        element: <RegisterPage />,
      },
    ],
  },
]);

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <UserContextProvider>
        <RouterProvider router={router} />
      </UserContextProvider>
      <ToastContainer />
    </QueryClientProvider>
  );
}

export default App;
