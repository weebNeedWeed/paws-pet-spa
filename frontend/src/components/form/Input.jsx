import clsx from "clsx";
import PropTypes from "prop-types";
import { useId } from "react";
import { useFormContext } from "react-hook-form";

function Input({ label, name, placeholder, type, className, validation }) {
  const {
    register,
    formState: { errors },
  } = useFormContext();
  const id = useId();
  const finalClassName = clsx(className, "text-liver");

  const errorMessage = errors[name] ? errors[name].message : "";

  return (
    <div className={finalClassName}>
      <label htmlFor={id} className="font-semibold">
        {label}
      </label>
      <input
        id={id}
        type={type}
        placeholder={placeholder}
        className="outline-none border-liver border-2 w-full p-3 rounded"
        {...register(name, validation)}
      />
      <span className="text-sm font-normal text-red-700">{errorMessage}</span>
    </div>
  );
}

Input.propTypes = {
  label: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  placeholder: PropTypes.string.isRequired,
  type: PropTypes.string.isRequired,
  className: PropTypes.string,
  validation: PropTypes.object,
};

export default Input;
