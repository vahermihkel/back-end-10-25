/* eslint-disable @typescript-eslint/no-explicit-any */
import { useTranslation } from "react-i18next";
import { toast } from "react-toastify";

function useFetch() {
  const { t } = useTranslation(); // kui poleks seda rida, oleks UTIL sees

  async function makeQuery(endpoint: string, apiMethod: string, payload: any, message: string) {
    try {
        const res = await fetch(import.meta.env.VITE_BACKEND_URL + endpoint, {
          method: apiMethod,
          body: JSON.stringify(payload),
          headers: {
            "Authorization": "Bearer " + sessionStorage.getItem("token"),
            "Content-Type": "application/json"
          }
        });
        if (res.status === 403) {
          toast.error(getErrorMessage("not-enough-rights"));
          return;
        }
        const json = await res.json();
        if (json.message && json.status && json.timestamp) {
          toast.error(getErrorMessage(json.message)); // ilusam on läbi tõlke neid välja kuvada
        } else {
          toast.success(t("success." + message));
        }
      } catch(error) {
        console.log(error);
      }
  }

  function getErrorMessage(message: string) {
    const errorMessage = t("error." + message);
    if (errorMessage.startsWith("error.")) { // ei saanud tõlkida
      return t("error.generic"); // üldine veateade
    } else {
      return errorMessage; // sai tõlkida, backendist tulnud tõlgitud veateade
    }
  }

  return (
    makeQuery
  )
}

export default useFetch