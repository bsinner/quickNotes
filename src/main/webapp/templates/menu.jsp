<div class="ui huge menu">
    <div class="header item">
        <a href="editor" id="homeLink">Quick Notes</a>
    </div>
    <div class="right menu">
        <a href="#" class="item" id="signIn">Sign In</a>
        <a href="#" class="item">Sign Out</a>
    </div>
</div>

<%--This div contains the sign in menu and is hidden by default--%>
<div class="ui tiny modal" id="loginModal">
    <i class="icon-cancel" id="loginExit"></i>
    <div class="header">
        Sign In
    </div>
    <div class="content">
        <form class="ui form" method="POST" action="j_security_check">
            <div class="field">
                <label>E-mail</label>
                <input type="text" name="j_username" id="email">
            </div>
            <div class="field">
                <label>Password</label>
                <input type="password" name="j_password" id="password">
            </div>
            <input type="submit" class="ui button" id="loginBtn" value="Sign In"/>
        </form>
    </div>

</div>
