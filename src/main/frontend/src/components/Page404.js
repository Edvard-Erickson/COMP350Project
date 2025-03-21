// fallback page for any unknown URL
import { useEffect, useState } from 'react'
import { Link } from "react-router-dom"

const Page404 = () => {
    return (
        <div id="err">
            <h1>404 Error</h1>
            <p>Page not Found.</p>
        </div>
    )
}

export default Page404;