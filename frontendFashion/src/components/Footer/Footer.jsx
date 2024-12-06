import React from "react";
import FbIcon from "../common/FbIcon";
import InstaIcon from "../common/InstaIcon";
import ZaloIcon from "../common/zaloIcon";
import YouTubeIcon from "../common/YoutubeIcon";

const Footer = ({ content }) => {
  return (
    <div className="bg-[#fff9c4] text-black py-8">
      <div className="flex justify-around">
        {content?.items &&
          content?.items?.map((item, index) => {
            return (
              <div className="flex flex-col">
                <p className="text-[16px] pb-[10px]">{item?.title}</p>
                {item?.list &&
                  item?.list?.map((listItem, index) => (
                    <a
                      className="flex flex-col text-[12px] py-2"
                      href={listItem?.path}
                    >
                      {listItem?.label}
                    </a>
                  ))}
                {item?.description && <p>{item?.description}</p>}
              </div>
            );
          })}
      </div>
      <div className="flex gap-2 items-center justify-center py-4">
        <a href="/fb">
          <FbIcon />
        </a>
        <a href="/zalo">
          <ZaloIcon />
        </a>
        <a href="/insta">
          <InstaIcon />
        </a>
        <a href="/youtube">
          <YouTubeIcon />
        </a>
      </div>
      <p className="text-sm text-black text-center content-center">
        {content?.copyright}
      </p>
    </div>
  );
};

export default Footer;
