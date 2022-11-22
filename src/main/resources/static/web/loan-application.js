const app = Vue.createApp({
    data() {
        return {
            tarjetasInfo: [{ nombre: "Hipotecario", img: "./img/casa.jpg", interes: 20 }, { nombre: "Personal", img: "./img/personal.jpg", interes: 13 }, { nombre: "Automotriz", img: "./img/auto.jpg", interes: 10 }], /* , { nombre: "Estudiantil", img: "./img/estudiantil.jpg", interes: 20 }, { nombre: "Credito", img: "./img/credito.jpg", interes: 30 }*/
            info: [],
            prestamos: [],
            prestamo: [],
            montoMaximo: 0,
            prestamoCuotas: [],
            vModelCuota: 0,
            vModelPrest: "",
            vModelMonto: 0,
            cuentasCliente: [],
            vModelCuenta: "",
            valorCuota: 0,
            deudaFinal: 0,
            prestamoInteres: 0,

        }
    },
    created() {
        this.loadPrestamos()
        this.loadCliente()

    },
    mounted() {
    },
    methods: {
        loadPrestamos() {
            axios
                .get('/api/loans')
                .then(response => {
                    this.prestamos = response.data
                    this.tarjetasInfo.forEach(imagen => {
                        this.prestamos.forEach(prestamo => {
                            if (imagen.nombre == prestamo.name) {
                                Object.assign(imagen, prestamo)
                            }
                        })
                    })
                    console.log(this.tarjetasInfo)
                })
                .catch(error => console.log(error))
        },
        loadCliente() {
            axios
                .get('/api/clients/current')
                .then(response => {
                    this.cuentasCliente = response.data.accounts.sort((a, b) => { if (a.id > b.id) { return 1 } if (a.id < b.id) { return -1 } })
                })
        },
        solicitarPrestamo() {
            axios.post('/api/loans', { id: `${this.vModelPrest}`, amount: `${this.vModelMonto}`, payments: `${this.vModelCuota}`, account: `${this.vModelCuenta}` })
                .then((response) => {
                    console.log(response)
                    if (response.status == 201) {
                        Swal.fire({
                            text: `Prestamo solicitado con exito.`,
                            confirmButtonColor: 'lightgreen',
                            willClose: () => {
                                window.location.assign("./accounts.html")
                            }
                        })
                    }

                    if (response.status == 202) {
                        Swal.fire({
                            text: `Prestamo solicitado, lo revisaremos lo antes posible.`,
                            confirmButtonColor: 'lightgreen',
                            willClose: () => {
                                window.location.assign("./accounts.html")
                            }
                        })
                    }
                })
                .catch(function (error) {
                    console.log(error)
                    if (error.response.data == "Loan already in use") {
                        Swal.fire({
                            text: `Ya solicitaste este prestamo`,
                            confirmButtonColor: 'lightgreen',
                        })
                    }

                    if (error.response.data == 'Max max amount') {
                        Swal.fire({
                            text: `Supera el monto maximo para este prestamo.`,
                            confirmButtonColor: 'lightgreen',
                        })
                    }

                    if (error.response.data == "Missing amount") {
                        Swal.fire({
                            text: `El prestamo debe ser mayor a 0 y menor al monto máximo`,
                            confirmButtonColor: 'lightgreen',
                        })
                    }
                })
        },
        modificarSaldo(saldo) {
            return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(saldo);
        },
        toStrings(array) {
            if (array != undefined) {
                return array.join(", ").toString()
            }
        },
        cerrarSesion() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
                .catch(error => console.log(error))
        },
    },
    computed: {
        vModels() {
            if (this.vModelPrest != "") {
                this.prestamo = this.prestamos.filter(prestamo => prestamo.id == this.vModelPrest)[0]
                this.montoMaximo = this.prestamo.maxAmount
                this.prestamoCuotas = this.prestamo.payments
                this.prestamoInteres = this.prestamo.interest.toString().slice(2,)
            }

            if (this.vModelMonto != 0 && this.vModelCuota != 0) {

                let interes = 0;

                interes = this.vModelMonto * this.prestamo.interest

                this.deudaFinal = interes
                this.valorCuota = interes / this.vModelCuota
            }
        }

    }
}).mount('#app')