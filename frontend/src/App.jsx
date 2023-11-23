import { RouterProvider } from "react-router-dom";
import { createBrowserRouter } from "react-router-dom";
import DefaultLayout from "./components/ui/DefaultLayout";
import AuthLayout from "./components/ui/AuthLayout";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/Authentication/LoginPage";

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
    ],
  },
]);

function App() {
  return (
    <div>
      <RouterProvider router={router} />
    </div>
  );
}

export default App;
