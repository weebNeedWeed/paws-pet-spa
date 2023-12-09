import { Helmet } from "react-helmet-async";
import { BookingContextProvider } from "../../contexts/BookingContext";
import { useAuthenticatedRoute } from "../../hooks";
import BasicInformationForm from "./BasicInformationForm";
import ChoosingServiceForm from "./ChoosingServiceForm";
import ChoosingTimeForm from "./ChoosingTimeForm";

function BookingPage() {
  useAuthenticatedRoute();

  return (
    <div className="text-liver pb-32">
      <Helmet>
        <title>Đặt hẹn | PAWS</title>
      </Helmet>

      <h1 className="text-liver text-5xl font-extrabold text-center uppercase">
        Đặt hẹn
      </h1>

      <div className="container max-w-3xl mx-auto">
        <BookingContextProvider>
          <BasicInformationForm />

          <ChoosingServiceForm />

          <ChoosingTimeForm />
        </BookingContextProvider>
      </div>
    </div>
  );
}

export default BookingPage;
