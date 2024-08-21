"use client";

import { useEffect } from 'react';

interface AddressSearchProps {
  onSelectAddress: (address: string) => void;
}

const AddressSearch = ({ onSelectAddress }: AddressSearchProps) => {
  useEffect(() => {
    const script = document.createElement('script');
    script.src = "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
    script.async = true;
    document.body.appendChild(script);

    script.onload = () => {
      const daum = (window as any).daum;
      const postcode = new daum.Postcode({
        oncomplete: (data: any) => {
          onSelectAddress(data.address);
        }
      });

      document.getElementById("search-address")?.addEventListener("click", () => {
        postcode.open();
      });
    };

    return () => {
      document.body.removeChild(script);
    };
  }, [onSelectAddress]);

  return (
    <button id="search-address" className="bg-blue-500 text-white p-2 m-2">
      주소 검색
    </button>
  );
};

export default AddressSearch;
