import React from "react";
import HeroImg from "../../assets/img/BackGroundHeRo1.jpg";
import { NavLink } from "react-router-dom";
const HeroSection = () => {
  return (
    <div
      className="relative flex items-center bg-cover flex-start bg-center text-left h-svh w-full"
      style={{ backgroundImage: `url(${HeroImg})` }}
    >
      <div className="absolute top-0 right-0 bottom-0 left-0"></div>
      <main className="flex flex-col justify-end px-10 lg:px-24 z-10 w-full">
        <div className="text-right">
          <h2 className="text-2xl text-white transition-all duration-300 hover:text-4xl">
            Stylish Tops
          </h2>
        </div>
        <div className="text-right">
          <p className=" text-white sm:mt-5 text-6xl transition-all duration-300 hover:text-7xl">
            The Cool Collection
          </p>
        </div>
        <div className="text-right">
          <p className=" text-white sm:mt-5 text-2xl transition-all duration-300 hover:text-3xl">
            Fresh / Fun / Free
          </p>
        </div>
        <div className="text-right  ">
          <button className="relative group border rounded mt-6 border-black hover:bg-white hover:text-yellow-500 hover:border-yellow-500 text-white bg-black w-44 h-12">
            <NavLink
              to="/men"
              className={({ isActive }) => (isActive ? "active-link" : "")}
            >
              Shop Now
            </NavLink>
            {/* Tooltip hiển thị khi hover */}
            <div className="absolute -bottom-10 left-0 w-40 bg-transparent text-black text-center text-sm rounded py-1 opacity-0 group-hover:opacity-100 transition-opacity duration-300">
              Go to men's fashion
            </div>
          </button>
        </div>
      </main>
    </div>
  );
};

export default HeroSection;
