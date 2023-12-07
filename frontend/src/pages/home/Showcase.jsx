import { Link } from "react-router-dom";
import PropTypes from "prop-types";
import clsx from "clsx";

function Showcase({ src, headingLabel, to, btnLabel, isReversed }) {
  return (
    <div
      className={clsx(
        "flex items-center",
        isReversed ? "flex-row-reverse" : "flex-row"
      )}
    >
      <div className="flex flex-col items-center gap-y-1">
        <h1 className="uppercase text-6xl w-96 font-extrabold text-center text-liver">
          {headingLabel}
        </h1>

        <Link
          to={to}
          className="bg-desaturatedCyan text-bone px-4 py-2 mt-4 rounded w-fit"
        >
          {btnLabel}
        </Link>
      </div>
      <img src={src} alt="showcase" className="w-[32rem]" />
    </div>
  );
}

Showcase.propTypes = {
  src: PropTypes.string.isRequired,
  headingLabel: PropTypes.string.isRequired,
  to: PropTypes.string.isRequired,
  btnLabel: PropTypes.string.isRequired,
  isReversed: PropTypes.bool,
};

export default Showcase;
