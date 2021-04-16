const util = {
    isEmpty: (value) => {
        if (value === null) return true
        if (typeof value === 'undefined') return true
        if (typeof value === 'string' && value === '') return true
        if (Array.isArray(value) && value.length < 1) return true
        if (typeof value === 'object' && value.constructor.name === 'Object' && Object.keys(value).length < 1 && Object.getOwnPropertyNames(value) < 1) return true
        if (typeof value === 'object' && value.constructor.name === 'String' && Object.keys(value).length < 1) return true // new String()

        return false
    },

    ifEmpty: function (value, fallback) {
        return this.isEmpty(value) ? fallback : value;
    }

};

// const empty = (value) => {
//     if (value === null) return true
//     if (typeof value === 'undefined') return true
//     if (typeof value === 'string' && value === '') return true
//     if (Array.isArray(value) && value.length < 1) return true
//     if (typeof value === 'object' && value.constructor.name === 'Object' && Object.keys(value).length < 1 && Object.getOwnPropertyNames(value) < 1) return true
//     if (typeof value === 'object' && value.constructor.name === 'String' && Object.keys(value).length < 1) return true // new String()
//
//     return false
// };
// https://to2.kr/cmu