const app = Vue.createApp({
    data() {
        return {
            dolarHoy: {},
            nombre: "",
            apellido: "",
            email: "",
            contraseña: "",
        }
    },
    created() {
        this.dolar()
    },
    mounted() {
    },
    methods: {
        dolar() {
            axios
                .get("https://www.dolarsi.com/api/api.php?type=valoresprincipales")
                .then(response => {
                    this.dolarHoy = response.data[0].casa
                })

                .catch(error => console.log(error))
        },
        iniciarSesion() {
            axios.post('/api/login', `email=${this.email}&password=${this.contraseña}`).then(response => window.location.assign("./accounts.html"))
                .catch(function (error) {
                    console.log(error.response)
                    Swal.fire({
                        text:'Email o contraseña incorrecta',
                        confirmButtonColor: 'lightgreen',})
                })
        },
        registrarse() {
            if(this.email.includes("@")){
            axios.post('/api/clients', `firstName=${this.nombre}&lastName=${this.apellido}&email=${this.email}&password=${this.contraseña}`).then(response =>
                this.iniciarSesion())
            .catch(function (error) {
                if (error.response.data == "Missing first name") {
                    return Swal.fire({
                        text:'Falta completar el campo nombre',
                        confirmButtonColor: 'lightgreen',})
                }
                
                if(error.response.data == "Missing last name"){
                    return Swal.fire({
                        text:'Falta completar el campo apellido',
                        confirmButtonColor: 'lightgreen',})
                }
                
                if(error.response.data == "Missing email"){
                    return Swal.fire({
                        text:'Falta completar el email',
                        confirmButtonColor: 'lightgreen',})
                }
                
                if(error.response.data == "Missing password"){
                    return Swal.fire({
                        text:'Falta completar el password',
                        confirmButtonColor: 'lightgreen',})
                }
                if(error.response.data == "Email already in use"){
                    return Swal.fire({
                        text:'Email ya registrado',
                        confirmButtonColor: 'lightgreen',})
                }
                console.log(error)
            }) } else
            {
                Swal.fire({
                    text:'El Email debe contener @',
                    confirmButtonColor: 'lightgreen',})
            }
        },
        /*toast() {
            const toastTrigger = document.getElementById('liveToastBtn')
            const toastLiveExample = document.getElementById('liveToast')
            if (toastTrigger) {
                toastTrigger.addEventListener('click', () => {
                    const toast = new bootstrap.Toast(toastLiveExample)

                    toast.show()
                })
            }
        },
        modoOscuro() {
            console.log("hola")
            //const BTNSWITCH = document.querySelector('#switch')
            console.log(document.body.classList)

            document.querySelector('#switch').addEventListener('click', () => {
                console.log(document.body.classList)
                document.body.classList.toggle('dark')
                document.querySelector('#switch').classList.toggle('active')
            })
        },*/
    },
    computed: {
    }
}).mount('#app')