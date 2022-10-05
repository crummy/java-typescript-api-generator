import { UsersApi } from "../../target/ts/api"

const usersApi = new UsersApi();

function init() {
    updateUsers()
    const usernameField: HTMLInputElement = document.querySelector("#username")!!
    const createUserForm: HTMLFormElement = document.querySelector("#create-user")!!
    createUserForm.addEventListener('submit',  (e) => {
        e.preventDefault()
        usersApi.addUser(usernameField.value).then((user) => {
            setResult(`User ${user.userId} created`)
            updateUsers()
        }).catch(e => setResult("An error occurred: " + e))
    })
}

function setResult(message: string) {
    const result = document.querySelector("#create-user-result")!!
    result.textContent = message;
    setTimeout(() => result.textContent == "", 5000)
}

function updateUsers() {
    const header: HTMLHeadingElement = document.querySelector(".header")!!
    const usersList: HTMLUListElement = document.querySelector(".users-list")!!
    usersApi.getUsers().then(users => {
        header.innerHTML = `${users.length} Users`
        usersList.innerHTML = users.map(u => `<li>${u.userId}: ${u.username}</li>`).join("\n")
    })
}

init()