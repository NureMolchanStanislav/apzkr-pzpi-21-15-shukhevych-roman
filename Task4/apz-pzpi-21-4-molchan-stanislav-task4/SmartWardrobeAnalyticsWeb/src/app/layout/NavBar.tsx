import { observer } from "mobx-react-lite";
import { Button, Container, Menu, Dropdown, DropdownMenu } from "semantic-ui-react";
import 'semantic-ui-css/semantic.min.css';
import { useEffect } from "react";
import { useStore } from "../stores/store";
import { Link } from "react-router-dom";

export default function NavBar() {
    const { userStore: { user, logout, isLoggedIn, getUser } } = useStore();

    useEffect(() => {
        if (isLoggedIn && !user) {
            getUser();
        }
    }, [isLoggedIn, user, getUser]);

    const handleLogout = () => {
        logout();
        window.location.reload();
    };

    return (
        <Menu inverted fixed='top'>
            <Container>
                <Menu.Item header style={{ position: 'relative' }}>
                    <div style={{
                        width: '120px',
                        height: '120px',
                        position: 'absolute',
                        top: '0px',
                        left: '-150px',
                        overflow: 'hidden',
                        backgroundColor: 'white'
                    }}>
                        <img src="/assets/logo.png" alt="logo" style={{ width: '100%', height: '100%' }} />
                    </div>
                    Wardrobe
                </Menu.Item>
                <Menu.Item name="Collections"/>
                <Menu.Item>
                    <Button positive content='Create collection'/>
                </Menu.Item>

                {isLoggedIn || user != null ? (
                    <Menu.Item position='right'>
                        <Dropdown pointing='top left' text={user?.email}>
                            <DropdownMenu>
                                <Dropdown.Item as={Link} to={`/user/profile/${user?.id}`} text='My Profile' icon='user' />
                                <Dropdown.Item as={Link} to={`/user/offers`} text='My Offers' />
                                <Dropdown.Item onClick={handleLogout} text='Logout' />
                            </DropdownMenu>
                        </Dropdown>
                    </Menu.Item>
                ) : (
                    <Menu.Item position='right'>
                        <Button as={Link} to="/login" content="Login" />
                        <Button as={Link} to="/register" content="Register" style={{ marginLeft: '0.5em' }} />
                    </Menu.Item>
                )}
            </Container>
        </Menu>
    );
}
