const app = Vue.createApp({
    data() {
        return {
            queryString: window.location.search.substring(4,),
            url: '/api/accounts/',
            cuenta: [],
            transacciones: [],
            transaccionModal: [],
            fechaTrans: ""
        }
    },
    created() {
        this.loadData(this.url + this.queryString)
    },
    mounted() {
    },
    methods: {
        loadData(url) {
            axios
                .get(url)
                .then(response => {
                    this.cuenta = response.data
                    this.transacciones = this.cuenta.transaction.sort((a, b) => { if (a.id > b.id) { return -1 } if (a.id < b.id) { return 1 } })
                })

                .catch(error => console.log(error))
        },
        detalles(mov){
            this.transaccionModal = mov
            console.log(this.transaccionModal)
            this.fechaTrans = mov.date.slice(0,10) + " " + mov.date.slice(11,16)
        },
        cerrarSesion(){
            axios.post('/api/logout').then(response => console.log('signed out!!!')).catch(error => console.log(error))
        },
        modificarSaldo(saldo) {
            return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(saldo);
        },
    },
    computed: {
    }
}).mount('#app')

