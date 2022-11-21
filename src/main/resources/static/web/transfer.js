const app = Vue.createApp({
    data() {
        return {
            cuentas: [],
            cuentaDestinoPersonal: [],
            balance: 0,
            cuentaInput: "",
            numeroCuentaOrigen: "",
            numeroCuentaDestino:"",
            monto: 0,
            descripcion: "",
        }
    },
    created() {
        this.loadData()
    },
    mounted() {
    },
    methods: {
        loadData(){
            axios
                .get('/api/clients/current')
                .then(response => {
                    this.cuentas = response.data.accounts.sort((a, b) => { if (a.id > b.id) { return 1 } if (a.id < b.id) { return -1 } })
                    console.log(this.cuentas)
                })
                .catch(error => console.log(error))
        },
        transferencia(){
            axios.post("/api/transactions", `amount=${this.monto}&description=${this.descripcion}&originNum=${this.numeroCuentaOrigen}&destinyNum=${this.numeroCuentaDestino.toUpperCase()}`)
            .then(response => Swal.fire({
                text:`Transacción realizada con éxito.`,
                confirmButtonColor: 'lightgreen',}))
            .then(response => window.location.assign("./accounts.html"))
            .catch(function (error) {
                if (error.response.data == "Missing amount") {
                    Swal.fire({
                        text:`Monto incorrecto.`,
                        confirmButtonColor: 'lightgreen',})
                }
                if (error.response.data == "Missing description") {
                    Swal.fire({
                        text:"Descripción incompleta.",
                        confirmButtonColor: 'lightgreen',})
                }
                if (error.response.data == "Missing source account") {
                    Swal.fire({
                        text:`Cuenta de origen incorrecta.`,
                        confirmButtonColor: 'lightgreen',})
                }
                if (error.response.data == "Missing destination account") {
                    Swal.fire({
                        text:"Cuenta de destino incorrecta.",
                        confirmButtonColor: 'lightgreen',})
                }
                if (error.response.data == "The destination account is the same") {
                    Swal.fire({
                        text:`La cuenta de destino es igual a la de origen.`,
                        confirmButtonColor: 'lightgreen',})
                }
                if (error.response.data == "The origin account does not exist") {
                    Swal.fire({
                        text:"La cuenta de origen no existe.",
                        confirmButtonColor: 'lightgreen',})
                }
                if (error.response.data == "The destination account does not exist") {
                    Swal.fire({
                        text:`La cuenta de destino es incorrecta.`,
                        confirmButtonColor: 'lightgreen',})
                }
                if (error.response.data == "Insufficient balance") {
                    Swal.fire({
                        text:"Saldo insuficiente para realizar la transacción.",
                        confirmButtonColor: 'lightgreen',})
                }
            })
        },
        cerrarSesion() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
                .catch(error => console.log(error))
        },
    },
    computed: {
        cuentaDestinoPropia(){
            if(this.numeroCuentaOrigen != ""){
            this.balance = this.cuentas.filter(cuenta => cuenta.number == this.numeroCuentaOrigen)[0].balance
            this.cuentaDestinoPersonal = this.cuentas.filter(cuenta => cuenta.number != this.numeroCuentaOrigen)
            }
        },
    }
}).mount('#app')