<div class="ui huge menu">
    <div class="header item">
        <a href="editor" id="homeLink">Quick Notes</a>
    </div>
    <div class="right menu">
        <a href="#" class="item" id="signIn">Sign In</a>
        <a href="#" class="item">Sign Out</a>
    </div>
</div>

<%--login popup--%>
<div class="ui tiny modal" id="loginModal">
    <i class="icon-cancel" id="loginExit"></i>
    <div class="header">
        Sign In
    </div>
    <div class="content">
        <form class="ui form" method="POST" action="<%=request.getContextPath()%>/api/login">
            <div class="field">
                <label>E-mail</label>
                <input type="text" id="email" name="email">
            </div>
            <div class="field">
                <label>Password</label>
                <input type="password" id="password" name="password">
            </div>
            <input type="submit" class="ui button" id="loginBtn" value="Sign In"/>
        </form>
    </div>

</div>
