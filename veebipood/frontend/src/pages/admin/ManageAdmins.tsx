import { useEffect, useState } from "react";
import useLoadItems from "../../hooks/useLoadItems"
import { Person } from "../../models/Person";

function ManageAdmins() {
  const [persons, setPersons] = useState<Person[]>([]);
  const {items: dbPersons, loading} = useLoadItems<Person>("/persons", true);
  
  useEffect(() => {
    setPersons(dbPersons);
  }, [dbPersons]);

  function changeAdmin(person: Person) {
    fetch(import.meta.env.VITE_BACKEND_URL + `/change-admin?id=${person.id}`, {
      method: "PATCH",
      headers: {
        "Authorization": "Bearer " + sessionStorage.getItem("token")
      }
    })
      .then(res => res.json())
      .then(json => {
        setPersons(json);
      })
  }

  if (loading) {
    return <div>Loading...</div>
  }

  return (
    <div>
      {persons.map(person => 
        <div key={person.id}>
          <div>{person.id}</div>
          <div>{person.firstName} {person.lastName}</div>
          <div>{person.email}</div>
          <div>{person.role}</div>
          <button disabled={person.role === "SUPERADMIN"} onClick={() => changeAdmin(person)}>Change role</button>
        </div>
      )}
    </div>
  )
}

export default ManageAdmins