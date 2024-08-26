"use client";

import { useEffect, useRef } from 'react';

interface AddressSearchProps {
    btn: string;
    setPlace: (address: string) => void;
}

const AddressSearch = ({ btn, setPlace }: AddressSearchProps) => {
    const buttonRef = useRef<HTMLButtonElement>(null);
    useEffect(() => {
        const script = document.createElement('script');
        script.src = "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
        script.async = true;
        document.body.appendChild(script);

        script.onload = () => {
            const daum = (window as any).daum;
            const postcode = new daum.Postcode({
                oncomplete: (data: any) => {
                    console.log(data.address);
                    setPlace(data.address);
                }
            });

            buttonRef.current?.addEventListener("click", () => {
                postcode.open();
            });
        };

        return () => {
            if (buttonRef.current) {
                buttonRef.current.removeEventListener("click", () => {});
            }
            document.body.removeChild(script);
        };
    }, [setPlace]);

    return (
        <button ref={buttonRef} className="bg-blue-500 text-white p-2 m-2">
            {btn}
        </button>
    );
};

export default AddressSearch;
