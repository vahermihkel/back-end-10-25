import { useEffect, useState } from "react";
// import { useTranslation } from "react-i18next";
import { ToastContainer } from "react-toastify";
import useFetch from "../../hooks/useFetch";
import { Person } from "../../models/Person";

function Profile() {
  const [person, setPerson] = useState<Person>({email: "", password: "", firstName: "", lastName: "", role: ""});
  const [passwordCredentials, setPasswordCredentials] = useState({});
  // const { t } = useTranslation();
  const backendQuery = useFetch();

  useEffect(() => {
      async function load() {
        try {
          const res = await fetch(import.meta.env.VITE_BACKEND_URL + "/person", {
            headers: {
              "Authorization": "Bearer " + sessionStorage.getItem("token")
            }
          });
          const json = await res.json();
          setPerson(json);
          setPasswordCredentials({"id": json.id});
          console.log(json);
        } catch(error) {
          console.log(error);
        }
      }
      load();
    }, []);

  return (
    <div>
      <label>First name</label> <br />
      <input defaultValue={person.firstName} onChange={(e) => setPerson({...person, "firstName": e.target.value})} type="text" /> <br />
      <label>Last name</label> <br />
      <input defaultValue={person.lastName} onChange={(e) => setPerson({...person, "lastName": e.target.value})} type="text" /> <br />
      <label>Email</label> <br />
      <input defaultValue={person.email} onChange={(e) => setPerson({...person, "email": e.target.value})} type="text" /> <br />
      <button onClick={() => backendQuery("/persons", "PUT", person, "updated-profile")}>Update</button>
      <ToastContainer />
      <br /><br /><br />

      <label>Old Password</label> <br />
      <input type="password"
        onChange={(e) => setPasswordCredentials({...passwordCredentials, 
                              "oldPassword": e.target.value})} /> <br />
      <label>New Password</label> <br />
      <input type="password" 
        onChange={(e) => setPasswordCredentials({...passwordCredentials, 
                              "newPassword": e.target.value})} /> <br />
      <label>Confirm new Password</label> <br />
      <input type="password"
        onChange={(e) => setPasswordCredentials({...passwordCredentials, 
                              "confirmPassword": e.target.value})}  /> <br />
      <button onClick={() => backendQuery("/update-password", "PATCH", passwordCredentials, "updated-password")}>Muuda parool</button>
    </div>
  )
}

export default Profile