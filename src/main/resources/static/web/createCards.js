const app = Vue.createApp({
    data() {
        return {
            nombreCliente: "",
            color: "",
            tipo: "",
            cuentasCliente: [],
            accountvModel: "",
        }
    },
    created() {
        this.clientCurrent()
    },
    mounted() {
    },
    methods: {
        clientCurrent() {
            axios.get('/api/clients/current')
                .then(response => {
                    console.log(response)
                    this.nombreCliente = response.data.firstName + " " + response.data.lastName
                    this.cuentasCliente = response.data.accounts
                    console.log(this.cuentasCliente)
                })
                .catch(error => error)
        },
crearTarjeta() {
    let tipo = this.tipo.toLowerCase() + "o"
    let color = this.color.toLowerCase()
    axios.post('/api/clients/current/cards', `color=${this.color}&typo=${this.tipo}&numAccount=${this.accountvModel}`)
        .then(response =>
            Swal.fire({
                text: 'Tarjeta creada con éxito.',
                confirmButtonColor: 'lightgreen',
                willClose: () => {
                    window.location.assign("./cards.html")
                }
            }))
        .catch(function (error) {
            if (error.response.data == "Max cards typo") {
                Swal.fire({
                    text: `Tenés el máximo permitido de tarjetas de ${tipo}.`,
                    confirmButtonColor: 'lightgreen',
                })
            }
            console.log(error)
            if (error.response.status == 400) {
                Swal.fire({
                    text: "Tenés que completar todos los campos",
                    confirmButtonColor: 'lightgreen',
                })
            }
            if (error.response.data == "Max cards color") {
                Swal.fire({
                    text: `Tenés el máximo permitido de tarjetas de ${tipo}, ${color}.`,
                    confirmButtonColor: 'lightgreen',
                })
            }
            if (error.response.data == "Missing account number") {
                Swal.fire({
                    text: `Tenes que elegir una cuenta para asociar tu tarjeta.`,
                    confirmButtonColor: 'lightgreen',
                })
            }
            console.log(error.response)

        })
},
cerrarSesion() {
    axios.post('/api/logout').then(response => console.log('signed out!!!'))
        .catch(error => console.log(error))
},
    },
computed: {
}
}).mount('#app')