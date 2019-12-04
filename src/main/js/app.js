const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {employees: []};
	}

	componentDidMount() {
		client({method: 'GET', path: '/api/seeks'}).done(response => {
            console.log(response.entity);
            
			this.setState({seeks: response.entity});
		});
	}

	render() {
		return (
			<div>Hello App!</div>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)
