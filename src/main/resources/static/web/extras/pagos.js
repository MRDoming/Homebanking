const app = Vue.createApp({
    data() {
        return {
            numeroTarjetavModel: "",
            cvvvModel: 0,
            montovModel: 0,
            descripcionvModel: "",
            numeroTarjeta: ""
        }
    },
    created() {
    },
    mounted() {
    },
    methods: {
        realizarPago() {
            axios.post('/api/payments', { number: `${this.numeroTarjetavModel}`, cvv: `${this.cvvvModel}`, amount: `${this.montovModel}`, description: `${this.descripcionvModel}` })
                .then(response => {
                    Swal.fire({
                        text: 'Pago realizado con éxito',
                        confirmButtonColor: 'lightgreen',
                        willClose: () => {
                            window.location.assign("./pagos.html")
                          }
                    })
                })
                .catch(error => {
                    console.log(error)

                        Swal.fire({
                            text: 'Error, por favor verificar los datos ingresados',
                            confirmButtonColor: 'lightgreen',
                        })
                    
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