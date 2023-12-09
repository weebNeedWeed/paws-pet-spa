import { FormProvider, useFieldArray, useForm } from "react-hook-form";
import { Button, Input, RadioButton } from "../../components/form";
import PropTypes from "prop-types";
import ServiceCheckbox from "./ServiceCheckbox";
import { useEffect } from "react";
import { useGetServices, useGetPetTypes } from "../../hooks";
import { toast } from "react-toastify";
import { useBookingContext } from "../../contexts/BookingContext";
import { FaCheck } from "react-icons/fa";

const petNameValidation = {
  required: {
    value: true,
    message: "Tên không được rỗng",
  },
};

function PetInformationForm({ petNum }) {
  const methods = useForm();
  const {
    handleSubmit,
    control,
    setError,
    formState: { errors },
  } = methods;
  const [bookingState, bookingDispatch] = useBookingContext();

  const { fields, replace } = useFieldArray({
    control,
    name: "service",
  });

  const {
    isError: isServicesError,
    data: services,
    isSuccess: isServicesSuccess,
  } = useGetServices();

  const {
    isError: isPetTypesError,
    data: petTypes,
    isSuccess: isPetTypesSuccess,
  } = useGetPetTypes();

  useEffect(() => {
    if (isServicesSuccess) {
      const formElms = services.data.map((svc) => ({
        id: svc.id,
        name: svc.name,
        value: false,
      }));
      replace(formElms);
    }
  }, [isServicesSuccess]);

  useEffect(() => {
    if (isServicesError || isPetTypesError) {
      toast.error("Có lỗi xảy ra, vui lòng thử lại");
    }
  }, [isServicesError, isPetTypesError]);

  if (bookingState.handlingPetNum < petNum) {
    return <></>;
  }

  const serviceErrorMessage = errors["service"]
    ? errors["service"].message
    : "";

  const isAbleToSubmit = bookingState.handlingPetNum === petNum;
  const onSubmit = handleSubmit((data) => {
    if (!isAbleToSubmit) return;

    if (data.service.every((svc) => svc.value === false)) {
      setError("service", {
        type: "required",
        message: "Vui lòng chọn dịch vụ",
      });
      return;
    }

    bookingDispatch({
      type: "UPDATE_PET_INFORMATION",
      payload: {
        ...data,
        petType: parseInt(data.petType, 10),
        service: data.service
          .filter((svc) => svc.value === true)
          .map((svc) => svc.id),
      },
    });
  });

  return (
    <FormProvider {...methods}>
      <h4 className="text-2xl my-4 font-semibold">
        2.{petNum} Thú cưng {petNum}
      </h4>

      <form onSubmit={onSubmit}>
        <div className="max-w-sm">
          <Input
            name="name"
            label="- Tên thú cưng"
            placeholder="Nhập tên thú cưng"
            type="text"
            validation={petNameValidation}
          />
        </div>

        {isPetTypesSuccess && (
          <div className="flex flex-row items-center gap-x-4 mt-4">
            {petTypes.data.map((type, index) => (
              <RadioButton
                checked={index === 0}
                name="petType"
                value={type.id.toString()}
                label={type.name}
                key={index}
              />
            ))}
          </div>
        )}

        <div className="mt-4">
          <h4 className="font-semibold">- Chọn dịch vụ</h4>

          <div className="flex flex-row gap-x-5">
            {fields.map((field, index) => (
              <ServiceCheckbox name="service" key={field.id} index={index} />
            ))}
          </div>
          <span className="text-sm font-normal text-red-700">
            {serviceErrorMessage}
          </span>
        </div>

        {isAbleToSubmit && (
          <Button
            type="submit"
            className="mt-4 flex flex-row gap-x-2 items-center"
          >
            <FaCheck />
            Tiếp tục
          </Button>
        )}
      </form>
    </FormProvider>
  );
}

PetInformationForm.propTypes = {
  petNum: PropTypes.number.isRequired,
};

export default PetInformationForm;
