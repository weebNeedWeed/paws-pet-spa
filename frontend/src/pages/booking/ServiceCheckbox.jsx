import PropTypes from "prop-types";
import { useFormContext } from "react-hook-form";
import { useState } from "react";
import pawsSvg from "./../../assets/paws.svg";
import clsx from "clsx";
import { FaCheck } from "react-icons/fa";

function ServiceCheckbox(props) {
  const { register, setValue, getValues, clearErrors } = useFormContext();
  const { index, name } = props;
  const [checked, setChecked] = useState(false);

  const handleClick = () => {
    clearErrors("service");
    setChecked((old) => {
      setValue(`${name}.${index}.value`, !old);
      return !old;
    });
  };

  const key = `${name}.${index}`;

  return (
    <div>
      <input {...register(`${key}.value`)} type="checkbox" className="hidden" />
      <button
        type="button"
        onClick={handleClick}
        className={clsx(
          "transition-all flex flex-col items-center justify-center border-liver border-2 rounded-xl w-32 h-32",
          checked && "bg-camel"
        )}
      >
        <img src={pawsSvg} alt="paws" className="w-14" />

        <p className="text-lg font-medium">{getValues(`${key}.name`)}</p>

        <span
          className={clsx(
            "transition-all",
            checked ? "opacity-100" : "opacity-25"
          )}
        >
          <FaCheck />
        </span>
      </button>
    </div>
  );
}

ServiceCheckbox.propTypes = {
  index: PropTypes.number.isRequired,
  name: PropTypes.string.isRequired,
};

export default ServiceCheckbox;
