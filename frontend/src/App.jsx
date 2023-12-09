import { RouterProvider } from "react-router-dom";
import { createBrowserRouter } from "react-router-dom";
import { DefaultLayout, AuthLayout } from "./components/ui";
import HomePage from "./pages/home/HomePage";
import LoginPage from "./pages/authentication/LoginPage";
import RegisterPage from "./pages/authentication/RegisterPage";
import { QueryClient, QueryClientProvider } from "react-query";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { UserContextProvider } from "./contexts/UserContext";
import BookingPage from "./pages/booking/BookingPage";
import { HelmetProvider } from "react-helmet-async";
import NotFoundPage from "./pages/NotFoundPage";

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
  {
    path: "*",
    element: <NotFoundPage />,
  },
]);

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <HelmetProvider>
        <UserContextProvider>
          <RouterProvider router={router} />
        </UserContextProvider>
      </HelmetProvider>
      <ToastContainer />
    </QueryClientProvider>
  );
}

export default App;
