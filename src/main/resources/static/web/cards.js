const app = Vue.createApp({
    data() {
        return {
            url: '/api/clients/current',
            cliente: {},
            cuentas: [],
            balanceTotal: 0,
            tarjetas: [],
            tarjetasDebito: [],
            tarjetasCredito: [],
            
        }
    },
    created() {
        this.loadData(this.url)
    },
    mounted() {
    },
    methods: {
        loadData(url) {
            axios
                .get(url)
                .then(response => {
                    this.cliente = response.data
                    this.cuentas = this.cliente.accounts
                    this.cuentas.forEach(cuenta => this.balanceTotal += cuenta.balance)
                    this.tarjetas = this.cliente.cards.sort((a, b) => { if (a.id > b.id) { return 1 } if (a.id < b.id) { return -1 } })
                    console.log(this.tarjetas)

                    this.tarjetasDebito = this.tarjetas.filter(tarjeta => tarjeta.type == "DEBIT")
                    this.tarjetasCredito = this.tarjetas.filter(tarjeta => tarjeta.type == "CREDIT")
                })

                .catch(error => console.log(error))
        },
        cerrarSesion(){
            axios.post('/api/logout').then(response => console.log('signed out!!!')).catch(error => console.log(error))
        }
    },
    computed: {
    }
}).mount('#app')