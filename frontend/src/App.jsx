import { RouterProvider } from "react-router-dom";
import { createBrowserRouter } from "react-router-dom";
import DefaultLayout from "./components/ui/DefaultLayout";
import AuthLayout from "./components/ui/AuthLayout";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/Authentication/LoginPage";
import RegisterPage from "./pages/Authentication/RegisterPage";
import { QueryClient, QueryClientProvider } from "react-query";
import { ToastContainer } from "react-toastify";

const router = createBrowserRouter([
  {
    path: "/",
    element: <DefaultLayout />,
    children: [{ index: true, element: <HomePage /> }],
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
      <RouterProvider router={router} />
      <ToastContainer />
    </QueryClientProvider>
  );
}

export default App;
