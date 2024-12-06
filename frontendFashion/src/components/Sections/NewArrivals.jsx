import React from "react";
import SectionHeading from "./SectionsHeading/SectionHeading";
import Jeans from "../../assets/img/jeans1.jpg";
import Shirts from "../../assets/img/shirts.jpg";
import Tshirt from "../../assets/img/t-shirts.jpg";
import dresses from "../../assets/img/dress.jpg";
import short from "../../assets/img/short.jpg";
import hoodie from "../../assets/img/hoodie.jpg";
import sweater from "../../assets/img/sweater.jpg";
import Card from "../Card/Card";
import Carousel from "react-multi-carousel";
import { responsive } from "../../utils/Section.constants";
import "./NewArrivals.css";

const items = [
  {
    title: "Jeans",
    imagePath: Jeans,
  },
  {
    title: "Shirts",
    imagePath: Shirts,
  },
  {
    title: "T-Shirts",
    imagePath: Tshirt,
  },
  {
    title: "Dresses",
    imagePath: dresses,
  },
  {
    title: "Joggers",
    imagePath: require("../../assets/img/jogger.jpg"),
  },
  {
    title: "Ao Dai",
    imagePath: require("../../assets/img/aodai.jpg"),
  },
  {
    title: "Shorts",
    imagePath: short,
  },
  {
    title: "Hoodies",
    imagePath: hoodie,
  },
  {
    title: "Sweater",
    imagePath: sweater,
  },
];
const NewArrivals = () => {
  return (
    <>
      <SectionHeading title={"New Arrivals"} />
      <Carousel
        responsive={responsive}
        autoPlay={false}
        swipeable={true}
        draggable={false}
        showDots={false}
        infinite={false}
        partialVisible={false}
        itemClass={"react-slider-custom-item"}
        className="px-8"
      >
        {items &&
          items?.map((item, index) => (
            <Card
              key={item?.title + index}
              title={item.title}
              imagePath={item.imagePath}
            />
          ))}
      </Carousel>
    </>
  );
};

export default NewArrivals;
