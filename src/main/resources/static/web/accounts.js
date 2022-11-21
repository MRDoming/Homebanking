const app = Vue.createApp({
    data() {
        return {
            url: '/api/clients/current',
            cliente: {},
            cuentas: [],
            balanceTotal: 0,
            usuario: "",
            contrasenia: "",
            linkAcounts: "",
            movimientosCuentas: [],
            movimientosOrdenados: [],
            transaccionModal: {},
            fechaTrans: "",
            loans: [],
            botonPrestamos: [],
            aprobar: [],
            tarjetas: [],
            nuevoEmail: "",
            nuevaContraseña: "",
            ok: "ok",
            rejected: "rejected",
            clientesAdmin: [],
            clienteBuscado: [],
            clienteBuscadoVModel: [],
            nombrePrestamovModel: "",
            maxAmountvModel: 0,
            cuotasvModel: [],
            otroCuotasvModel: 0,
            interesvModel: 1,
        }
    },
    created() {
        this.loadData(this.url,{headers:{'accept':'application/xml'}}) /*,{headers:{'accept':'application/xml'}}*/
    },
    mounted() {
    },
    methods: {
        loadData(url) {
            axios
                .get(url)
                .then(response => {
                    console.log(response)
                    this.cliente = response.data
                    this.cuentas = this.cliente.accounts.sort((a, b) => { if (a.id > b.id) { return 1 } if (a.id < b.id) { return -1 } })
                    this.cuentas.forEach(cuenta => this.balanceTotal += cuenta.balance)
                    this.transacciones()
                    this.loans = this.cliente.loans.sort((a, b) => { if (a.id > b.id) { return 1 } if (a.id < b.id) { return -1 } })
                    this.botonPrestamos = this.loans.filter(loan => loan.status == "ok" || loan.status == "outstanding" || loan.status == "rejected")
                    this.tarjetas = this.cliente.cards.sort((a, b) => { if (a.id > b.id) { return 1 } if (a.id < b.id) { return -1 } })
                    this.loadPrestamos()
                    console.log(this.cliente.cards)
                })
                .catch(function (error) {
                    console.log(error)
                    if (error.response.status == 403 && error.response.data.status == "Max-accounts") {
                        return Swal.fire({
                            text: 'Tenés más de las cuentas permitidas, por favor comunicate con CoperBank a la brevedad.',
                            confirmButtonColor: 'lightgreen',
                        }).then(() => axios.post('/api/logout').then(response => window.location.assign("./index.html")))
                    }
                })
        },
        loadPrestamos() {
            if (this.cliente.email == 'admin@mindhub.com') {
                axios
                    .get('/api/clientloan')
                    .then(response => {
                        this.prestamos = response.data
                        this.aprobar = this.prestamos.filter(loan => loan.status == 'outstanding')
                    }).then(() => {
                        axios
                            .get('/api/clients')
                            .then(res => {
                                this.clientesAdmin = res.data

                                this.clientesAdmin.forEach(cliente => {
                                    cliente.accounts.forEach(account => {
                                        this.aprobar.forEach(prestamo => {
                                            if (account.number == prestamo.cuenta) {
                                                prestamo["client"] = cliente.email
                                                prestamo["idClient"] = cliente.id
                                            }
                                        })

                                    })
                                })

                                console.log(this.clientesAdmin)

                            })
                            .catch(error => console.log(error))
                    })
                    .catch(error => console.log(error))
            }
        },
        transacciones() {
            this.cliente.accounts.forEach(cuenta => {
                cuenta.transaction.forEach(transaccion => {
                    transaccion['numeroCuenta'] = cuenta.number
                    this.movimientosCuentas.push(transaccion)
                })
            })
            this.movimientosOrdenados = this.movimientosCuentas.sort((a, b) => { if (a.id > b.id) { return -1 } if (a.id < b.id) { return 1 } })
        },
        detalles(mov) {
            this.transaccionModal = mov
            this.fechaTrans = mov.date.slice(0, 10) + " " + mov.date.slice(11, 16)
        },
        modificarSaldo(saldo) {
            return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(saldo);
        },
        cerrarSesion() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
                .catch(error => console.log(error))
        },
        crearCuenta() {
            axios.post('/api/clients/current/accounts').then(response => window.location.assign("./accounts.html"))
                .catch(function (error) {
                    if (error.response.data == "Max accounts") {
                        return Swal.fire({
                            text: 'Tenés el número máximo de cuentas permitidas.',
                            confirmButtonColor: 'lightgreen',
                        })
                    }
                })
        },
        editUsuario() {
            axios.patch('/api/clients/current', `password=${this.nuevaContraseña}&email=${this.nuevoEmail}`)
                .then(response => Swal.fire({
                    text: 'Cambios guardados, por favor reiniciá sesión',
                    confirmButtonColor: 'lightgreen',
                }))
                .then(response => {
                    this.cerrarSesion()
                    window.location.assign("./index.html")
                }
                )
                .catch(function (error) {
                    if (error.response.data == "Email already in use") {
                        return Swal.fire({
                            text: 'Ese email ya está registado en nuestra base de datos.',
                            confirmButtonColor: 'lightgreen',
                        })
                    }

                    if (error.response.data == "No changes") {
                        return Swal.fire({
                            text: 'Completá los datos que desees cambiar.',
                            confirmButtonColor: 'lightgreen',
                        })
                    }
                })
        },
        crearPrestamo() {
            if(this.otroCuotasvModel != 0){
                this.cuotasvModel.push(this.otroCuotasvModel)
            }
            if(this.interesvModel > 1 && this.interesvModel <= 2){
            axios.post('/api/loan', `name=${this.nombrePrestamovModel}&maxAmount=${this.maxAmountvModel}&payments=${this.cuotasvModel}&interest=${this.interesvModel}`)
                .then(response => {
                    console.log(response)
                    Swal.fire({
                        text: 'Prestamo creado',
                        confirmButtonColor: 'lightgreen',
                        willClose: () => {
                            window.location.assign("./accounts.html")
                        }
                    })
                })
                .catch((error) => {
                    Swal.fire({
                        text: `Error`,
                        confirmButtonColor: 'lightgreen',
                    })
                    
                })
            } else {
                Swal.fire({
                    text: `El interés debe ser mayor a 1 y menor o igual a 2`,
                    confirmButtonColor: 'lightgreen',
                })
            }
        },
        aprobarPrestamo(prestamo) {
            axios.patch('/api/clientloan', `id=${prestamo}&status=${this.ok}`)
                .then(response => {
                    console.log(response)
                    Swal.fire({
                        text: 'Cambios guardados, prestamo aprobado.',
                        confirmButtonColor: 'lightgreen',
                        willClose: () => {
                            window.location.assign("./accounts.html")
                        }
                    })
                })
                .catch(error => console.log(error))
        },
        rechazarPrestamo(prestamo) {
            axios.patch('/api/clientloan', `id=${prestamo}&status=${this.rejected}`)
                .then(response => {
                    console.log(response)
                    Swal.fire({
                        text: 'Cambios guardados, prestamo rechazado.',
                        confirmButtonColor: 'lightgreen',
                        willClose: () => {
                            window.location.assign("./accounts.html")
                        }
                    })
                })
                .catch(error => console.log(error))
        },
        eliminarPrestamo(prestamo) {
            console.log(prestamo)
            axios.patch('/api/clientloanelim', `id=${prestamo.id}`)
                .then(response => Swal.fire({
                    text: 'Prestamo eliminado.',
                    confirmButtonColor: 'lightgreen',
                    willClose: () => {
                        window.location.assign("./accounts.html")
                    }
                }))
                .catch(error => console.log(error))
        }
        /*modoOscuro() {
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
        clienteAdmin() {
            this.clienteBuscado = this.clientesAdmin.filter(cliente => cliente.id == this.clienteBuscadoVModel)
            console.log(this.clienteBuscado)
        }
    }
}).mount('#app')